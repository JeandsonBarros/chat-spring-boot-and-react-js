import { getToken } from "./TokenService";
import api from './api'

export async function getChats(page=0) {
    try {
        const response = await api.get(`/chat/?size=30&page=${page}`,
            {
                headers: {
                    'Authorization': getToken(),
                }
            })

        return response.data

    } catch (error) {
        console.log(error)
    }
}

export async function getMessagesUser(userEmail, page=0){
    try {
        const response = await api.get(`/chat/user/${userEmail}?page=${page}`,
            {
                headers: {
                    'Authorization': getToken(),
                }
            })

        return response.data

    } catch (error) {
        console.log(error)
    }
}

export async function postMessage(emailRecipient, text) {
    try {
        await api.post(`/chat/`, { emailRecipient, text },
            {
                headers: {
                    'Authorization': getToken(),
                }
            }
        )

    } catch (error) {
        console.log(error);
    }
}