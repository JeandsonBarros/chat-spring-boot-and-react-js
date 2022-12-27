import './ChatStyles.css';

import { Button, Input, Modal, Text, User } from '@nextui-org/react';
import { useCallback, useEffect, useRef, useState } from 'react';
import { BiSend } from 'react-icons/bi';
import { BsListStars } from 'react-icons/bs';
import SockJsClient from 'react-stomp';

import { getUserData } from '../../../services/AuthService';
import { getChats, getMessagesUser, postMessage } from '../../../services/MessageService';
import ListChats from './ListChats';
import ListMessagesByUser from './ListMessagesByUser';
import SendMessage from './SendMessage';
import { getToken } from '../../../services/TokenService';

const headers = {
    'Authorization': getToken(),
    'Accept': 'application/json',
    'Content-Type': 'application/json',
    'Access-Control-Allow-Origin': '*',
    'Access-Control-Request-Method': '*'
}

export default function Messages() {

    const [authUser, setAuthUser] = useState({})

    const [message, setMessage] = useState('')
    const [recipientEmail, setRecipientEmail] = useState('');

    const [messagesFromChat, setMessagesFromChat] = useState();
    const [chats, setChats] = useState();
    const [conversationUser, setConversationUser] = useState();

    const messagesEndRef = useRef(null)

    const [visivleChats, setVisivleChats] = useState(false)

    useEffect(() => {
        listChats(0)
    }, [])

    async function listChats(page) {

        const authData = await getUserData()
        setAuthUser(authData)

        let data = await getChats(page)

        if (page > 0)
            data.content = data.content.concat(chats.content)

        let chatsList = await Promise.all(data.content.map(async chat => {

            const userChat = chat.user1.email === authData.email ? chat.user2 : chat.user1
            const lastMessage = await getMessagesUser(userChat.email, 0, 1)
            chat['lastMessage'] = lastMessage.content[0].text

            return chat

        }));

        data.content = chatsList
        setChats(data)

    }

    async function sendOneMessage() {

        await postMessage(recipientEmail, message)
        listChats(0)

        setMessage('')
        setRecipientEmail('')

        if (conversationUser)
            getChatByUser(conversationUser)

    }

    async function getChatByUser(user, page) {

        setVisivleChats(false)

        setConversationUser(user)
        setRecipientEmail(user.email)

        let data = await getMessagesUser(user.email, page)

        if (page > 0)
            data.content = data.content.concat(messagesFromChat.content)

        data.content.sort((date1, date2) => {
            return new Date(date1.sendDateMessage) - new Date(date2.sendDateMessage)
        })

        setMessagesFromChat(data)

    }

    function asViewMessages() {

        if (messagesFromChat) {
            return (
                <div className='chat'>
                    <div className='conversationUser'>
                        {conversationUser &&
                            <User
                                bordered
                                as="button"
                                size="lg"
                                color="primary"
                                name={conversationUser.name}
                                description={conversationUser.email}
                                src={require('../../../imgs/user.webp')}
                                css={{ m: 5 }}

                            />
                        }
                    </div>

                    <hr />

                    <div className='messages' ref={messagesEndRef}>
                        <ListMessagesByUser
                            messagesFromChat={messagesFromChat}
                            authUser={authUser}
                            getChatByUser={getChatByUser}
                            conversationUser={conversationUser}
                        />
                    </div>

                    <div className='sendMessage'>
                        <Input
                            aria-label="Send Message"
                            contentRightStyling={false}
                            value={message}
                            fullWidth
                            placeholder="Message"
                            onChange={event => setMessage(event.target.value)}
                            contentRight={
                                <Button
                                    light
                                    rounded
                                    auto
                                    onPress={sendOneMessage}
                                    title="Send Message"
                                    disabled={message.length === 0}
                                >
                                    <BiSend style={{ fontSize: 20 }} />
                                </Button>}
                        />
                    </div>

                </div>
            )

        } else {
            return (
                <div className='noChat'>
                    <h3>Start a new chat!</h3>
                    <div style={{ width: 200 }}>
                        <SendMessage />
                    </div>
                </div>)
        }
    }

    const updateChats = () => {
        listChats(chats.number)
    }

    return (
        <>
            <div className='buttonShowChatList'>
                <Button
                    css={{ w: '100vw', borderRadius: 0 }}
                    shadow
                    onPress={() => {
                        listChats(0)
                        setVisivleChats(true)
                    }}
                >
                    <BsListStars style={{ marginRight: 5, fontSize: 22 }} /> Show chats
                </Button>
            </div>

            <Modal
                closeButton
                aria-labelledby="modal-title"
                open={visivleChats}
                onClose={() => setVisivleChats(false)}
            >

                <Modal.Header>
                    <Text b id="modal-title" size={20}>
                        Your chats
                    </Text>
                </Modal.Header>

                <Modal.Body>

                    <SendMessage updateChats={updateChats} />

                    {chats && <ListChats
                        chats={chats}
                        authUser={authUser}
                        getChatByUser={getChatByUser}
                        listChats={listChats}
                    />}

                </Modal.Body>

            </Modal>

            <section className='chats'>

                <SockJsClient
                    url='http://localhost:8080/ws-message'
                    headers={headers}
                    topics={[`/topic/message/${authUser.email}`]}
                    onConnect={() => console.log("conectado")}
                    onDisconnect={console.log("Disconnected!")}
                    onMessage={msg => {

                        if (chats) {
                            let tempListChats = { ...chats };
                            const index = tempListChats.content.findIndex(chat => {
                                const chatUser = chat.user1.email === authUser.email ? chat.user2 : chat.user1
                                return chatUser.email === msg.body.sender.email
                            })

                            tempListChats.content[index].lastMessage = msg.body.text

                            setChats(tempListChats)
                        }

                        //-------------------------------------------

                        if (conversationUser)
                            if (msg.body.sender.email === conversationUser.email) {
                                let tempMessagesFromChat = { ...messagesFromChat }
                                tempMessagesFromChat.content.push(msg.body)
                                setMessagesFromChat(tempMessagesFromChat)
                            }

                    }}
                    debug={false}
                />

                <nav className='navLeft'>

                    <SendMessage updateChats={updateChats} />

                    {chats && <ListChats
                        chats={chats}
                        authUser={authUser}
                        getChatByUser={getChatByUser}
                        listChats={listChats}
                    />}

                </nav>

                {asViewMessages()}

            </section>
        </>
    );
}
