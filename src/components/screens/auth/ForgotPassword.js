import { Button, Input, Loading, } from "@nextui-org/react";
import { useState } from "react";
import "./StylesAuth.css";
import { sendingEmailFogotPassword, changingForgottenPassword } from '../../../services/AuthService';
import Alert from "../../layouts/Alert";

function ForgotPassword() {

    const [loandingSendVisible, setLoandingSendVisible] = useState(false)
    const [loandingChange, setLoandingChange] = useState(false)
    const [alertChange, setAlertChange] = useState('')
    const [alertSendEmail, setAlertSendEmail] = useState({ message: '', sent: false })
    const [emailTo, setEmailTo] = useState('')
    const [changeForgottenPassword, setChangeForgottenPassword] = useState({})
    const [confirmPassword, setConfirmPassword] = useState('')

    function setValue(key, value) {
        let tempChangeForgottenPassword = changeForgottenPassword
        tempChangeForgottenPassword[key] = value
        setChangeForgottenPassword(tempChangeForgottenPassword)
    }

    async function sendEmail() {

        if (!emailTo) {
            setAlertSendEmail('Provide recovery email')
            return
        }

        setChangeForgottenPassword({ email: emailTo })

        setLoandingSendVisible(true)

        const data = await sendingEmailFogotPassword(emailTo)
        setAlertSendEmail(data)

        setLoandingSendVisible(false)

    }

    async function changePassword() {

        if (!changeForgottenPassword.email ||
            !changeForgottenPassword.code ||
            !changeForgottenPassword.newPassword) {
            setAlertChange('Do not leave empty fields!')
            return
        }

        if (changeForgottenPassword.newPassword !== confirmPassword) {
            setAlertChange('Passwords do not match!')
            return
        }

        setLoandingChange(true)

        const data = await changingForgottenPassword(changeForgottenPassword)
        setAlertChange(data)

        setLoandingChange(false)
    }


    return (
        <section>

            <h2 style={{ textAlign: 'center' }}>Forgot Password</h2>

            <div className="formLogin">

                <h3>Send email</h3>
                <span>A recovery code valid for 15 minutes will be sent to your email.</span>

                <Alert
                    visible={alertSendEmail.message.length > 0}
                    text={alertSendEmail.message}
                />

                <Input
                    onChange={event => setEmailTo(event.target.value)}
                    label="Email"
                    placeholder="exemple@email.com"
                    type="email"
                    bordered
                    color="primary"
                />

                {loandingSendVisible && <Loading type="points" />}

                <Button shadow auto onPress={sendEmail}>Send</Button>

            </div>

            <div style={{ height: 10 }}></div>

            <div className="formLogin">

                <h3>Password change</h3>
                <span>Enter the recovery code sent to your email and the new password.</span>

                <Alert
                    visible={alertChange.length > 0}
                    text={alertChange}
                />

                <Input
                    label="Code"
                    placeholder="1234567"
                    type="number"
                    onChange={event => setValue('code', event.target.value)}
                    bordered
                    color="primary"
                />

                <Input.Password
                    label="New password"
                    placeholder="chat12345"
                    onChange={event => setValue('newPassword', event.target.value)}
                    bordered
                    color="primary"
                />
                <Input.Password
                    label="Confirm password"
                    placeholder="chat12345"
                    onChange={event => setConfirmPassword(event.target.value)}
                    bordered
                    color="primary"
                />

                {loandingChange && <Loading type="points" />}

                <Button onPress={changePassword} shadow auto>Change</Button>

            </div>

            <div style={{ height: 10 }}></div>

        </section>
    );
}

export default ForgotPassword;