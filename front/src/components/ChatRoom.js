import React, { useState, useEffect } from "react"
import axios from "axios"
import { useNavigate, useParams } from "react-router"
import { shallowEqual, useSelector } from "react-redux"
import useWebSocket from "../components/useWebSocket"
let chatRoomTitle
function ChatRoom() {
  const { stompClient, messages, setMessages, notifications, setNotifications } = useWebSocket()
  const [newMessage, setNewMessage] = useState("")
  const [chatRooms, setChatRooms] = useState([])
  const [selectedRoomIds, setSelectedRoomIds] = useState([]) // 여러 채팅방 구독을 위한 배열
  const [recipient, setRecipient] = useState("") // 1:1 채팅을 위한 수신자
  const [notification, setNotification] = useState("")
  const { roomId } = useParams() // URL에서 roomId 가져오기
  const [currentRoomId, setCurrentRoomId] = useState()
  const [participantsList, setParticipantsList] = useState([]) // 사용자 목록
  const navigate = useNavigate()
  const [selectedUser, setSelectedUser] = useState("") // 선택된 사용자
  const username = useSelector((state) => state.userData.username, shallowEqual) // 유저네임 불러오기
  const currentUserId = useSelector((state) => state.userData.id, shallowEqual)

  const [chatRoom, setChatRoom] = useState({
    type: "GROUP",
    title: "",
    ownerId: "",
    participantsList: ["1"],
  })

  // 채팅방 목록을 불러오기 위한 useEffect
  useEffect(() => {
    axios
      .get("/api/chat/rooms")
      .then((response) => {
        setChatRooms(response.data)
      })
      .catch((error) => {
        console.error("Error fetching chat rooms", error)
      })

    axios
      .get("/api/v1/users/profile")
      .then((response) => {
        setParticipantsList(response.data) // 사용자 목록 설정
        console.log(response.data)
      })
      .catch((error) => {
        console.error("Error fetching users", error)
      })
  }, [roomId])

  // 알림 구독
  useEffect(() => {
    if (stompClient) {
      stompClient.subscribe("/notification", (message) => {
        const parsedNotification = JSON.parse(message.body)
        setNotifications((prevNotifications) => [...prevNotifications, parsedNotification])
      })
    }
  }, [stompClient])

  // 새로운 채팅방을 생성하는 함수
  const createChatRoom = (roomId) => {
    axios
      .post("/api/chat/rooms", chatRoom)
      .then((response) => {
        const newRoom = response.data
        console.log(response.data)
        // 새로운 방이 생성되었음을 알리는 알림 추가
        const notification = {
          message: `${username}님이 "${currentRoomId}" 방을 생성했습니다.`,
          timestamp: new Date().toLocaleString(),
        }
        setNotifications((prevNotifications) => [...prevNotifications, notification])

        // 채팅방 목록에 새로운 방 추가
        setChatRooms((prevRooms) => [...prevRooms, newRoom])

        // 생성된 채팅방으로 이동
        navigate(`/chatroom/${newRoom.id}`)
        selectRoom(newRoom.id) // 방 선택
      })
      .catch((error) => {
        console.error("Error creating chat room:", error)
      })
  }

  // 채팅방 선택시 메시지 불러오기
  const selectRoom = (roomId) => {
    setCurrentRoomId(roomId) // 새로운 상태 추가
    navigate(`/chatroom/${roomId}`) // 페이지 이동

    // 구독이 이미 추가된 방인지 확인 후, 중복 구독 방지
    if (!selectedRoomIds.includes(roomId)) {
      setSelectedRoomIds([...selectedRoomIds, roomId])

      // 새로운 방에 구독 추가
      if (stompClient) {
        stompClient.subscribe(`/topic/${currentRoomId}`, (message) => {
          const parsedMessage = JSON.parse(message.body)
          setMessages((prevMessages) => [...prevMessages, parsedMessage]) // 다른 사용자 메시지 추가
        })
      }
    }

    // // 이전 채팅 메시지를 가져옵니다
    // axios.get(`/api/chat/rooms/${roomId}/messages`)
    //     .then(response => {
    //         setMessages(response.data);
    //     })
    //     .catch(error => {
    //         console.error('Error fetching messages:', error);
    //     });
  }

  // 그룹 채팅 메시지 보내기
  const sendGroupMessage = () => {
    console.log("WebSocket connected:", stompClient.current)
    console.log("Room ID:", currentRoomId)
    console.log("New Message:", newMessage)

    if (newMessage.trim() && currentRoomId) {
      const message = {
        sender: username, // 현재 사용자 정보
        content: newMessage,
        timestamp: new Date().toISOString(),
      }
      // 메시지를 즉시 메시지 목록에 추가
      setMessages((prevMessages) => [...prevMessages, message])

      stompClient.send(`/app/chat.sendMessage/${currentRoomId}`, {}, JSON.stringify(message))

      // 알림 추가
      const notification = {
        message: `${username}님이 메시지를 보냈습니다.`,
        timestamp: new Date().toLocaleString(),
      }
      setNotifications((prevNotifications) => [...prevNotifications, notification])

      setNewMessage("")
    }
  }

  // 1:1 메시지 보내기
  const sendPrivateMessage = () => {
    if (stompClient && recipient) {
      const message = {
        chatRoomId: currentRoomId,
        sender: username, // 현재 사용자 정보
        content: newMessage,
        recipient,
        timestamp: new Date().toISOString(),
      }

      stompClient.send(`/app/chat.sendMessage/private`, {}, JSON.stringify(message))

      // 메시지를 즉시 메시지 목록에 추가
      setMessages((prevMessages) => [...prevMessages, message])
      // 알림 추가
      const notification = {
        message: `${username}님이 "${newMessage}" 메시지를 보냈습니다.`,
        timestamp: new Date().toLocaleString(),
      }
      setNotifications((prevNotifications) => [...prevNotifications, notification])

      setNewMessage("")
    }
  }

  // 알림 보내기
  const sendNotification = () => {
    if (stompClient) {
      const notification = {
        message: username,
        timestamp: new Date().toLocaleString(),
      }
      stompClient.send(`/app/notification`, {}, JSON.stringify(notification))
      setNotification("")
    }
  }

  return (
    <div style={{ display: "flex", flexDirection: "row" }}>
      <div style={{ width: "30%", padding: "10px", borderRight: "1px solid #ccc" }}>
        {/* test code */}
        <button
          onClick={() => {
            // selectedUser - > userProfileInfo
            if (selectedUser !== "") {
              console.log("length : ", chatRoom.participantsList.length)

              setChatRoom({
                ...chatRoom,
                title: "chatRoomTitle",
                ownerId: currentUserId,
                participantsList: [...chatRoom.participantsList, selectedUser],
              })
            }
            console.log(chatRoom)
            // TODO: 초대 버튼을 누를때 participantsList에 초대하는 사람의 id도 넣어야함
          }}>
          test test test
        </button>
        <br />
        <button
          onClick={() => {
            setChatRoom({
              ...chatRoom,
              title: "",
              ownerId: currentUserId,
              participantsList: [],
            })
            console.log(chatRoom)
          }}>
          reset test
        </button>
        {/* 사용자 초대 UI */}
        <div>
          <select onChange={(e) => setSelectedUser(e.target.value)} value={selectedUser}>
            <option value="">사용자를 선택하세요</option>
            {participantsList.map((user) => (
              <option key={user.id} value={user.id}>
                {user.nickname}
              </option>
            ))}
          </select>
          <button onClick={createChatRoom}>초대하기</button>
        </div>

        <h3>채팅방 목록</h3>
        {/* 채팅방 목록 */}
        <ul>
          {chatRooms.map((room) => (
            <li
              key={room.id}
              onClick={() => selectRoom(room.id)}
              style={{
                cursor: "pointer",
                padding: "10px",
                backgroundColor: selectedRoomIds === room.id ? "#f0f0f0" : "transparent",
              }}>
              {room.name}
            </li>
          ))}
        </ul>
      </div>

      <div style={{ width: "70%", padding: "10px" }}>
        <h3>채팅방</h3>
        <div style={{ height: "700px", overflowY: "scroll", border: "1px solid #ddd", padding: "10px" }}>
          {messages.map((message, index) => (
            <div key={index}>
              <strong>{message.sender}:</strong> {message.content}
              <br />
              {new Date(message.timestamp).toLocaleString()}
            </div>
          ))}
        </div>

        <input
          type="text"
          value={newMessage}
          onChange={(e) => setNewMessage(e.target.value)}
          placeholder="메시지를 입력하세요..."
          style={{ width: "80%" }}
        />
        <button onClick={sendGroupMessage} style={{ width: "20%" }}>
          그룹 전송
        </button>
        <button onClick={sendPrivateMessage} style={{ width: "20%" }}>
          1:1 전송
        </button>
      </div>

      <div style={{ width: "30%", padding: "10px", borderLeft: "1px solid #ccc" }}>
        <h3>알림</h3>
        <ul>
          {notifications.map((notification, index) => (
            <li key={index}>
              {notification.message} - {notification.timestamp}
            </li>
          ))}
        </ul>
        <button onClick={sendNotification}>알림 보내기</button>
      </div>
    </div>
  )
}

export default ChatRoom
