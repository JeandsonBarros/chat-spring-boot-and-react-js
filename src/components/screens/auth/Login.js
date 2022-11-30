import { Button, Input, Container, Loading } from '@nextui-org/react';
import "./StylesAuth.css";
import { BsEnvelopeFill, BsFillPersonFill, BsLockFill } from "react-icons/bs";
import { Link, useNavigate } from 'react-router-dom';
import { useEffect, useState } from 'react';
import { login } from '../../../services/AuthService';
import { getToken } from '../../../services/TokenService';
import Alert from '../../layouts/Alert';

function Login() {

    const [email, setEmail] = useState('')
    const [password, setPassword] = useState('')
    const navigate = useNavigate()
    const [alertVisible, setAlertVisible] = useState(false)
    const [alertText, setAlertText] = useState('')
    const [visibleLoading, setVisibleLoading] = useState(false)


    useEffect(() => {
        if (getToken())
            navigate('/')
    }, [])

    async function loginUser(event) {
        event.preventDefault()

        if (!email || !password) {
            setAlertText("Don't leave empty fields")
            alertVisible(true)
            return
        }

        setVisibleLoading(true)
        const response = await login(email, password)
        setVisibleLoading(false)

        if (response === 200)
            return navigate('/')

        setAlertText(response)
        setAlertVisible(true)

    }

    return (
        <section >

            <Container css={{ mt: 100, mb: 100 }}>

                <form onSubmit={loginUser} className='formLogin'>

                    <BsFillPersonFill className='userIconForm' />
                    <h1>Login</h1>

                    <Alert setVisible={setAlertVisible} visible={alertVisible} text={alertText} />
                    {visibleLoading && <Loading type="points" />}

                    <Input
                        fullWidth
                        id='email'
                        label="Email"
                        type="email"
                        bordered
                        color="primary"
                        placeholder="exemple@email.com"
                        onChange={event => setEmail(event.target.value)}
                        contentLeft={<BsEnvelopeFill />}
                    />

                    <div>
                        <Input.Password
                            id='password'
                            fullWidth
                            label="Password"
                            placeholder="chat12345"
                            bordered
                            color="primary"
                            onChange={event => setPassword(event.target.value)}
                            contentLeft={<BsLockFill />}
                        />
                        <Link>Forgot password?</Link>
                    </div>
                    
                    <Button shadow type='submit'>Login</Button>
                    <Link to="/register">Register</Link>

                </form>

            </Container>

        </section>
    );
}

export default Login;