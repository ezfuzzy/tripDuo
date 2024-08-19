import { useOutlet } from 'react-router-dom';
import './App.css';
import 'bootstrap/dist/css/bootstrap.css'
import BsNavBar from './components/BsNavBar';
import Footer from './components/Footer';


function App() {

  //현재 route 된 정보를 출력해주는 hook
  const currentOutlet = useOutlet()

  return (
    <div className="container">
      <BsNavBar />
      <div>{currentOutlet}</div>
      <Footer />
    </div>
  );
}

export default App;
