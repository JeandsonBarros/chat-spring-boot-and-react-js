
import { useNavigate, useLocation } from 'react-router-dom';
import { useEffect } from 'react';
import { getToken } from '../../services/TokenService';

function Container({ children }) {

    const navigate = useNavigate()
    const location = useLocation()

    useEffect(() => {
        
        if (location.pathname !== '/login' && location.pathname !== '/register' && !getToken())
                return navigate("/login")
        
    }, [location.pathname])

    return (
        <main>
            {children}
        </main>
    );
}

export default Container;