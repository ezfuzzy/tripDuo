import { useOutlet } from 'react-router-dom';
import './App.css';
import 'bootstrap/dist/css/bootstrap.css'
import BsNavBar from './components/BsNavBar';
import Footer from './components/Footer';
import LoginModal from './components/LoginModal';
import { useSelector } from 'react-redux';


function App() {
  const loginModal = useSelector(state => state.loginModal);
  //현재 route 된 정보를 출력해주는 hook
  const currentOutlet = useOutlet()

  return (
    <>
    <div className="container">
      <BsNavBar />
      <div>{currentOutlet}</div>
      
      <Footer />
    </div>
    <LoginModal show={loginModal.show} message={loginModal.message}/>
    </>
  );
}

export default App;
