import { setToken, getToken, removeToken } from "./TokenService";
import api from './api'

export async function login(email, password) {
    try {

        const response = await api.post('/auth/login', { email, password })

        if (response.status === 200) {
            setToken(response.data)
            console.log(response.data);
            return 200
        } else {
            return "Check that the credentials are correct."
        }

    } catch (error) {
        console.log(error);
        return "Error logging in, check that the credentials are correct."
    }
}

export async function userResgister(email, password, name) {
    try {

        await api.post('/auth/register', { email, password, name })

        const status = await login(email, password)
        return status === 200 ? 201 : status

    } catch (error) {
        console.log(error);
        return "Error registering."
    }
}

export async function getUserData() {
    try {

        const response = await api.get(
            '/auth/data',
            {
                headers: {
                    'Authorization': getToken(),
                }
            }
        )

        return response.data

    } catch (error) {
        console.log(error);
    }
}

export async function putUser(user) {
    try {

        const response = await api.put('/auth/update', user,
            {
                headers: {
                    'Authorization': getToken(),
                }
            }
        )

        return response.data

    } catch (error) {
        console.log(error);
        return "Error updating"
    }
}

export async function deleteUser() {
    try {

        const response = await api.delete("/auth/delete",
            {
                headers: {
                    'Authorization': getToken(),
                }
            })
        removeToken()
        return response.status
    } catch (error) {
        console.log(error);
        return error.status
    }
}