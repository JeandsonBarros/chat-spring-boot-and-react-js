import { getToken } from "./TokenService";
import api from './api'

export async function getMessages() {
    try {
        const response = await api.get(`/chat/`,
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

export async function getMessagesUser(userEmail){
    try {
        const response = await api.get(`/chat/user/${userEmail}`,
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