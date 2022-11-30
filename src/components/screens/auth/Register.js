import { useEffect, useState } from "react";
import { useNavigate, Link } from "react-router-dom";
import { getToken } from "../../../services/TokenService";
import "./StylesAuth.css";
import { Button, Input, Container, Loading } from '@nextui-org/react';
import { BsEnvelopeFill, BsFillPersonFill, BsLockFill, BsFillPersonLinesFill } from "react-icons/bs";
import Alert from '../../layouts/Alert';
import { userResgister } from '../../../services/AuthService';

function Register() {

    const navigate = useNavigate()
    const [name, setName] = useState('')
    const [email, setEmail] = useState('')
    const [password, setPassword] = useState('')
    const [confirmPassword, setConfirmPassword] = useState('')
    const [alertVisible, setAlertVisible] = useState(false)
    const [alertText, setAlertText] = useState('')
    const [visibleLoading, setVisibleLoading] = useState(false)

    useEffect(() => {
        if (getToken())
            navigate('/')
    }, [])

    async function registerUser(event) {
        event.preventDefault()

        if (!email || !password || !name || !confirmPassword) {
            setAlertText("Don't leave empty fields")
            setAlertVisible(true)
            return
        }

        if (password !== confirmPassword) {
            setAlertText("Passwords do not match")
            setAlertVisible(true)
            return
        }

        setVisibleLoading(true)
        const response = await userResgister(email, password, name)
        setVisibleLoading(false)

        if (response === 201) {
            setAlertVisible(false)
            return navigate('/')
        }

        setAlertText(response)
        setAlertVisible(true)

    }

    return (
        <section >
            <Container css={{ mt: 100, mb: 100 }}>

                <form onSubmit={registerUser} className='formLogin'>

                    <BsFillPersonFill className='userIconForm' />
                    <h1>Sign-up</h1>

                    <Alert setVisible={setAlertVisible} visible={alertVisible} text={alertText} />
                    {visibleLoading && <Loading type="points" />}

                    <Input
                        fullWidth
                        id='name'
                        label="Name"
                        type="text"
                        placeholder="Fulano"
                        bordered
                        color="primary"
                        onChange={event => setName(event.target.value)}
                        contentLeft={<BsFillPersonLinesFill />}
                    />

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

                    <Input.Password
                        onChange={event => setConfirmPassword(event.target.value)}
                        bordered
                        fullWidth
                        color="primary"
                        label="Confirm password"
                        placeholder="chat12345"
                        contentLeft={<BsLockFill />}
                    />

                    <Button shadow type='submit'>Sign-up</Button>
                    <Link to="/login">Login</Link>

                </form>

            </Container>
        </section>
    );
}

export default Register;