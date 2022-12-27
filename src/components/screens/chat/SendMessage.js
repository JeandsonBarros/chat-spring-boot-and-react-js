import { Button, Input, Modal, Text, Textarea } from "@nextui-org/react";
import { useState } from "react";
import { BsFillPlusCircleFill } from "react-icons/bs";
import { postMessage } from '../../../services/MessageService';

function SendMessage({ updateChats }) {

    const [modalMessageVisible, setModalMessageVisible] = useState(false)
    const [message, setMessage] = useState('')
    const [recipientEmail, setRecipientEmail] = useState('');

    async function sendMessage() {

        await postMessage(recipientEmail, message)

        setModalMessageVisible(false)
        setMessage('')
        setRecipientEmail('')
        updateChats()

    }

    return (
        <>
            <Button
                shadow
                onPress={() => setModalMessageVisible(true)}
                css={{ w: '100%' }}>
                <Text
                    size={20}
                    css={{
                        d: 'flex',
                        ai: 'center',
                        jc: 'center',
                    }}>
                    New chat
                    <BsFillPlusCircleFill style={{ marginLeft: 10 }} />
                </Text>
            </Button>

            <Modal
                closeButton
                open={modalMessageVisible}
                onClose={() => setModalMessageVisible(false)}
            >
                <Modal.Header>

                    <Text b size={18}>
                        Send new message
                    </Text>

                </Modal.Header>

                <Modal.Body>

                    <Input
                        label="Recipient's Email"
                        bordered
                        fullWidth
                        placeholder='exemple@email.com'
                        onChange={event => setRecipientEmail(event.target.value)}
                    />
                    <Textarea
                        rows={4}
                        label='Message'
                        bordered
                        fullWidth
                        placeholder='Exemple message'
                        onChange={event => setMessage(event.target.value)}
                    />

                </Modal.Body>

                <Modal.Footer>

                    <Button
                        color='error'
                        onPress={() => setModalMessageVisible(false)}
                        shadow
                        auto
                    >
                        Close
                    </Button>

                    <Button
                        shadow
                        auto
                        onPress={sendMessage}
                    >
                        Send
                    </Button>

                </Modal.Footer>

            </Modal>
        </>
    );
}

export default SendMessage;