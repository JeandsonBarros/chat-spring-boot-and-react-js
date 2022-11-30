import { Button, Input, Modal, Text } from "@nextui-org/react";
import { useEffect, useState } from "react";
import { getUserData, putUser, deleteUser } from "../../../services/AuthService";
import Alert from "../../layouts/Alert";
import ModalConfirm from "../../layouts/ModalConfirm";
import { useNavigate } from 'react-router-dom';
import { getToken } from "../../../services/TokenService";

function Account({ visible, setVisible }) {

    const navigation = useNavigate()
    const [name, setName] = useState('')
    const [email, setEmail] = useState('')
    const [alertVisible, setAlertVisible] = useState(false)
    const [alertText, setAlertText] = useState('')
    const [confirmVisible, setConfirmVisible] = useState(false)

    useEffect(() => {

        if (getToken()) {
            getUserData().then(data => {
                setName(data.name)
                setEmail(data.email)
            })
        }

    }, [visible])

    async function updateUser() {

        if (!name || !email)
            alert("Don't leave empty fields")

        const response = await putUser({ email, name })

        setAlertText(response)
        setAlertVisible(true)
    }

    async function deleteAccount() {
        const response = await deleteUser()
        if (response === 204) {
            setVisible(false)
            setConfirmVisible(false)
            navigation('/login')

        }
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
                    Account data
                </Text>
            </Modal.Header>

            <Modal.Body>

                <Alert setVisible={setAlertVisible} visible={alertVisible} text={alertText} />

                <Input
                    value={name}
                    onChange={(event) => setName(event.target.value)}
                    bordered
                    fullWidth
                    color="primary"
                    label="Name"
                    placeholder="Fulano"
                />

                <Input
                    value={email}
                    onChange={(event) => setEmail(event.target.value)}
                    bordered
                    fullWidth
                    color="primary"
                    label="Email"
                    placeholder="exemple@email.com"
                    type="email"
                />

                <hr />


                <ModalConfirm
                    setVisible={setConfirmVisible}
                    visible={confirmVisible}
                    message="Deleting this account will permanently remove all data, do you really want to continue?"
                    title="Remove account"
                    action={deleteAccount}
                />

            </Modal.Body>

            <Modal.Footer>

                <Button auto flat color="error" onPress={() => setVisible(false)}>
                    Close
                </Button>

                <Button onPress={updateUser} auto>
                    Update
                </Button>

                <Button
                    color="error"
                    shadow
                    onPress={() => setConfirmVisible(true)}
                >
                    Remove account
                </Button>

               

            </Modal.Footer>

        </Modal>

    );
}

export default Account;