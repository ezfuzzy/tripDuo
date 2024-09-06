import { useOutlet } from 'react-router-dom';
import 'bootstrap/dist/css/bootstrap.css';
import BsNavBar from './components/BsNavBar';
import Footer from './components/Footer';
import { useState } from 'react';

function App() {
  const [isPassAgreed, setIsPassAgreed] = useState(false)

  const currentOutlet = useOutlet();

  return (
    <div className="app-container">
      <BsNavBar />
      <div className="main-content">
        {currentOutlet}
      </div>
      <Footer />
    </div>
  );
}

export default App;
