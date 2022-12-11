import './ChatStyles.css';

import { Button, Card, Row, Text } from '@nextui-org/react';
import { useState } from 'react';
import { BsPlusCircle } from 'react-icons/bs';

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

export default ListMessagesByUser;