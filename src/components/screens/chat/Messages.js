import './ChatStyles.css';

import { Button, Card, Col, Input, Row, Text, User } from '@nextui-org/react';
import { useEffect, useRef, useState } from 'react';
import { BiSend } from 'react-icons/bi';
import SendMessage from './SendMessage';
import { getUserData } from '../../../services/AuthService';
import { getMessages, getMessagesUser, postMessage } from '../../../services/MessageService';

export default function Messages() {

    const [message, setMessage] = useState('')
    const [recipientEmail, setRecipientEmail] = useState('');
    const [chatByUser, setChatByUser] = useState();
    const [chats, setChats] = useState([]);
    const [authUser, setAuthUser] = useState({})
    const [conversationUser, setConversationUser] = useState();
    const messagesEndRef = useRef(null)

    useEffect(() => {

        const interval = setInterval(listChats, 1000);

        return () => {
            clearInterval(interval);
        };

    }, [])

    async function listChats() {

        const data = await getMessages()

        const authData = await getUserData()
        setAuthUser(authData)
        setChats(data.content)

    }

    async function sendMessage() {

        await postMessage(recipientEmail, message)
        listChats()

        setMessage('')
        setRecipientEmail('')

        if (conversationUser)
            getChatByUser(conversationUser)

    }

    function listMessagesByUser() {

        if (chatByUser) {

            return chatByUser.map((message, index) => {

                const isAuthUser = message.sender.email === authUser.email

                chatByUser.sort((date1, date2) => {
                    return new Date(date1.sendDateMessage) + new Date(date2.sendDateMessage)
                })

                return (
                    <Row
                        key={index}
                        justify={isAuthUser ? 'flex-end' : 'flex-start'}
                    >

                        <Card
                            css={{
                                br: isAuthUser ? "15px 15px 4px 15px" : "15px 15px 15px 4px",
                                w: 'auto',
                                mw: "330px",
                                m: 5,
                                backgroundColor: isAuthUser ? '$colors$primary' : '$colors$secondary',
                            }}
                        >
                            <Card.Body css={{ pt: 6, pb: 6, }}>

                                <Text>
                                    {message.text}
                                </Text>

                                <Text
                                    size={12}
                                    css={{
                                        textAlign: isAuthUser ? 'right' : 'start',
                                        color: 'rgba(255,255,255, 0.6)'
                                    }}>
                                    {(() => {
                                        const date = new Date(message.sendDateMessage)
                                        const minutes = date.getMinutes() < 10 ? "0" + date.getMinutes() : date.getMinutes()
                                        const hours = date.getHours() < 10 ? "0" + date.getHours() : date.getHours()
                                        return hours + ":" + minutes
                                    })()}
                                </Text>

                            </Card.Body>
                        </Card>

                    </Row>
                )
            })
        }
        else {
            return <></>
        }

    }

    async function getChatByUser(user) {

        setConversationUser(user)
        setRecipientEmail(user.email)

        const data = await getMessagesUser(user.email)
        setChatByUser(data.content)

    }

    function asViewMessages() {

        if (chatByUser) {
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
                                src="https://i.pravatar.cc/150?u=a042581f4e29026024d"
                                css={{ m: 5 }}

                            />
                        }
                    </div>

                    <hr />

                    <div className='messages' ref={messagesEndRef}>
                        {listMessagesByUser()}
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
                                    onPress={sendMessage}
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
            return <></>
        }
    }

    return (
        <section className='chats'>

            <ul className='chatList'>
                <li>
                    <SendMessage />
                </li>

                {chats.map(chat => {

                    const chatUser = chat.user1.email === authUser.email ? chat.user2 : chat.user1
                    let name = chatUser.name
                    if (name.length > 15)
                        name = name.slice(0, 15) + "..."

                    return (
                        <li key={chat.chatId} className='lineUser'>
                            <User
                                bordered
                                as="button"
                                size="lg"
                                color="primary"
                                name={name}
                                description={chat.messages[chat.messages.length - 1].text}
                                src="https://i.pravatar.cc/150?u=a042581f4e29026024d"
                                onClick={() => getChatByUser(chatUser)}
                            />
                        </li>
                    )
                })}

            </ul>

            {asViewMessages()}

        </section>
    );
}

