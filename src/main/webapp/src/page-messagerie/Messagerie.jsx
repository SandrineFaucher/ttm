import React, {useState, useMemo, useEffect} from "react";
import { Stomp } from '@stomp/stompjs';

export default function Messagerie(){
const [loadMessage, setLoadMessages ] = useState(false);
    const [loader, setLoader] = useState(false);
    const [messages, setMessages] = useState([]);
    const client = useMemo(()=>{
        return Stomp.client("ws://localhost:8080/ws")
    }, []);

    useEffect(()=>{
        setLoader(true);
        client.connect({}, ()=>{
            client.subscribe('/getMessages', (e)=>{
                console.log('Recieve Message', e.body)
                setMessages(JSON.parse(e.body))
            });
            client.subscribe('/updateMessages', (e)=>{
                console.log('Update Message', e.body)
            });
        })
    }, [client, loadMessage, setMessages, setLoader]);
    return (
        <>
            <h1>Page Messagerie</h1>
            <div className="card">
                <button onClick={() => client.send('/requestMessages', {}, "{}")}>
                </button>
                <ul>
                    {messages.map((message, index) => (<li key={`${message._id.timestamp}-${index}`}>
                        <p>{message.sender}</p>
                        <p>{message.dest}</p>
                        <p>{message.content}</p>
                    </li>))}
                </ul>
            </div>
        </>
    )
}