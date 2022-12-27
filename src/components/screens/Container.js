
import { useNavigate, useLocation } from 'react-router-dom';
import { useEffect } from 'react';
import { getToken, removeToken } from '../../services/TokenService';

function Container({ children }) {

    const navigate = useNavigate()
    const location = useLocation()

    useEffect(() => {
        // removeToken()
        if (
            location.pathname !== '/login' &&
            location.pathname !== '/forgot-password' &&
            location.pathname !== '/register' && 
            !getToken())
                return navigate("/login")
        
    }, [location.pathname])

    return (
        <main>
            {children}
        </main>
    );
}

export default Container;