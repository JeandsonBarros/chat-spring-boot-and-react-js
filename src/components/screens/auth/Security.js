import { Button, Input, Modal, Text } from "@nextui-org/react";
import { useState } from "react";
import { BsLockFill } from "react-icons/bs";
import { putUser } from "../../../services/AuthService";
import Alert from "../../layouts/Alert";

function Security({ visible, setVisible }) {

    const [password, setPassword] = useState('')
    const [confirmPassword, setConfirmPassword] = useState('')
    const [alertVisible, setAlertVisible] = useState(false)
    const [alertText, setAlertText] = useState('')

    async function updateUser() {

        if (!password || !confirmPassword) {
            setAlertText("Don't leave empty fields")
            setAlertVisible(true)
            return
        }

        if (password !== confirmPassword) {
            setAlertText("Passwords do not match")
            setAlertVisible(true)
            return
        }

        const response = await putUser({ password })

        setAlertText(response)
        setAlertVisible(true)
    }

    return (
        <Modal
            closeButton
            aria-labelledby="account-data"
            open={visible}
            onClose={() => setVisible(false)}
        >
            <Modal.Header>
                <Text b size={18}>
                    Security
                </Text>
            </Modal.Header>

            <Modal.Body>

                <Alert setVisible={setAlertVisible} visible={alertVisible} text={alertText} />

                <Input.Password
                    onChange={event => setPassword(event.target.value)}
                    bordered
                    fullWidth
                    label="Password"
                    color="primary"
                    placeholder="chat12345"
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

            </Modal.Body>

            <Modal.Footer>

                <Button auto flat color="error" onPress={() => setVisible(false)}>
                    Close
                </Button>

                <Button auto onPress={updateUser}>
                    Update
                </Button>

            </Modal.Footer>

        </Modal>);
}

export default Security;