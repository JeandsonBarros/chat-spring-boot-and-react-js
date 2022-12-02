import './ChatStyles.css';

import { Button, Card, Input, Modal, Row, Text, User } from '@nextui-org/react';
import { useEffect, useRef, useState } from 'react';
import { BiSend } from 'react-icons/bi';
import SendMessage from './SendMessage';
import { getUserData } from '../../../services/AuthService';
import { getChats, getMessagesUser, postMessage } from '../../../services/MessageService';
import { BsListStars, BsPlusCircle } from 'react-icons/bs';

function ListMessagesByUser({ messagesFromChat, authUser, getChatByUser, conversationUser }) {

    const [page, setPage] = useState(0)

    function pagitation() {
        getChatByUser(conversationUser, page + 1)
        setPage(page + 1)
    }

    function pagitationButton() {
        if (messagesFromChat.content.length < messagesFromChat.totalElements) {
            return (
                <Row justify='center' align='center' css={{ mt: 10 }}>
                    <Button
                        flat
                        title="Show more"
                        auto
                        onPress={pagitation}
                    >
                        <BsPlusCircle style={{ fontSize: 25 }} />
                    </Button>
                </Row>
            )
        }

        return <div></div>
    }

    return (
        <>

            <div>
                {messagesFromChat.content.map((message, index) => {

                    const isAuthUser = message.sender.email === authUser.email

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
                })}
            </div>

            {pagitationButton()}
        </>
    )
}

function ListChats({ authUser, getChatByUser, chats, listChats }) {

    const [page, setPage] = useState(0)

    function pagitation() {
        listChats(page + 1)
        setPage(page + 1)
    }

    function pagitationButton() {
        if (chats.content.length < chats.totalElements) {
            return (
                <Row justify='center' align='center' css={{ mt: 10 }}>
                    <Button
                        flat
                        title="Show more"
                        auto
                        onPress={pagitation}
                    >
                        <BsPlusCircle style={{ fontSize: 25 }} />
                    </Button>
                </Row>
            )
        }

        return <div></div>
    }


    return (
        <ul className='chatList'>

            {chats.content.map(chat => {

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
                            onClick={() => getChatByUser(chatUser, 0)}
                        />
                    </li>
                )
            })}

            <li>
                {pagitationButton()}
            </li>

        </ul>)
}

export default function Messages() {

    const [authUser, setAuthUser] = useState({})

    const [message, setMessage] = useState('')
    const [recipientEmail, setRecipientEmail] = useState('');

    const [messagesFromChat, setMessagesFromChat] = useState();
    const [chats, setChats] = useState();
    const [conversationUser, setConversationUser] = useState();

    const [intervalChats, setIntervalChats] = useState()
    const [intervalMessages, setIntervalMessages] = useState()
    

    const messagesEndRef = useRef(null)

    const [visivleChats, setVisivleChats] = useState(false)


    useEffect(() => {
        listChats(0)
    }, [])

    async function listChats(page=0) {

        if(intervalChats)
            clearInterval(intervalChats)

        const authData = await getUserData()
        setAuthUser(authData)

        let data = await getChats(page)

        if (page > 0)
            data.content = data.content.concat(chats.content)

        setChats(data)

        const insterval = setInterval(async () => {

            let data = await getChats(page)

            if (page > 0)
                data.content = data.content.concat(chats.content)

            setChats(data)
        }, 1000);

        setIntervalChats(insterval)

    }

    async function sendMessage() {

        await postMessage(recipientEmail, message)
        listChats(0)

        setMessage('')
        setRecipientEmail('')

        if (conversationUser)
            getChatByUser(conversationUser)

    }

    async function getChatByUser(user, page) {

        setVisivleChats(false)

        if (intervalMessages)
            clearInterval(intervalMessages)

        setConversationUser(user)
        setRecipientEmail(user.email)

        let data = await getMessagesUser(user.email, page)

        if (page > 0)
            data.content = data.content.concat(messagesFromChat.content)

        data.content.sort((date1, date2) => {
            return new Date(date1.sendDateMessage) - new Date(date2.sendDateMessage)
        })

        setMessagesFromChat(data)

        const interval = setInterval(async () => {

            let data = await getMessagesUser(user.email, page)

            if (page > 0)
                data.content = data.content.concat(messagesFromChat.content)

            data.content.sort((date1, date2) => {
                return new Date(date1.sendDateMessage) - new Date(date2.sendDateMessage)
            })

            setMessagesFromChat(data)
        }, 1000)

        setIntervalMessages(interval)

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
                                src="https://i.pravatar.cc/150?u=a042581f4e29026024d"
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
            return (
                <div className='noChat'>
                    <h3>Start a new chat!</h3>
                    <div style={{ width: 200 }}>
                        <SendMessage />
                    </div>
                </div>)
        }
    }

    return (
        <>
            <div className='buttonShowChatList'>
                <Button
                    css={{ w: '100vw', borderRadius: 0 }}
                    shadow
                    onPress={() => setVisivleChats(true)}
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
                    <SendMessage />
                    {chats && <ListChats
                        chats={chats}
                        authUser={authUser}
                        getChatByUser={getChatByUser}
                        listChats={listChats}
                    />}
                </Modal.Body>

            </Modal>

            <section className='chats'>

                <div className='navLeft'>

                    <SendMessage />

                    {chats && <ListChats
                        chats={chats}
                        authUser={authUser}
                        getChatByUser={getChatByUser}
                        listChats={listChats}
                    />}

                </div>

                {asViewMessages()}

            </section>
        </>
    );
}

