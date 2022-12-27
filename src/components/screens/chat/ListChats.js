import './ChatStyles.css';

import { Button, Row, User } from '@nextui-org/react';
import { BsPlusCircle } from 'react-icons/bs';
import { useEffect, useState } from 'react';
import { getMessagesUser } from '../../../services/MessageService';

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
                            description={chat.lastMessage}
                            src={require('../../../imgs/user.webp')}
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

export default ListChats;