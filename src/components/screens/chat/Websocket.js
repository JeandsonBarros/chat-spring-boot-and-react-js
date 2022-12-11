import React, { useEffect, useState } from 'react';
import { getToken } from '../../../services/TokenService';

//https://github.com/lahsivjar/react-stomp/issues/60
//https://www.npmjs.com/package/react-stomp
import SockJsClient from 'react-stomp';

const SOCKET_URL = 'http://localhost:8080/ws-message';

export default function Websocket() {
    const [message, setMessage] = useState('You server message here.');

    let onConnected = (event) => {
        console.log(event);
        console.log("Connected!!")
    }

    const headers = {
        'Authorization': getToken(),
        'Accept': 'application/json',
        'Content-Type': 'application/json',
        'Access-Control-Allow-Origin': '*',
        'Access-Control-Request-Method':'*'
    }

    return (
        <div>
            <SockJsClient
                url={SOCKET_URL}
                headers={headers}
                topics={['/topic/message']}
                onConnect={onConnected}
                onDisconnect={console.log("Disconnected!")}
                onMessage={msg => {
                    console.log(msg);
                    setMessage(msg.text);
                }}
                debug={false}
            />
            <div>{message}</div>

                {(()=>{
                    console.log(headers);
                })()}

        </div>
    );
}
