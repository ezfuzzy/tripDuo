import { useOutlet } from 'react-router-dom';
import './App.css';
import 'bootstrap/dist/css/bootstrap.css'
import BsNavBar from './components/BsNavBar';
import Footer from './components/Footer';


function App() {

  const currentOutlet = useOutlet();
  
  return (
    <>
      <div className="container">
        <BsNavBar />
        <div>{currentOutlet}</div>
        <Footer />
      </div>
      
    </>
  );
}

export default App;
