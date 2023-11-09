'use client';

import React, { useState } from 'react';
import SockJS from 'sockjs-client';
import { over } from 'stompjs';

type User = {
  username: string;
  connected: boolean;
  message: string;
};

type ChatMessage = {
  content: string;
  sender: string;
  messageType: string;
};

let stompClient: any = null;
export default function Home() {
  const [user, setUser] = useState<User>({
    username: '',
    connected: false,
    message: ''
  });
  const [chatMessages, setChatMessages] = useState<Array<ChatMessage>>([]);
  const [userName, setUserName] = useState<string>('');
  const [currentMessage, setCurrentMessage] = useState<string>('');

  const registerUser: () => void = () => {
    const socket: WebSocket = new SockJS('http://localhost:8080/ws');
    stompClient = over(socket);
    stompClient.connect({}, onConnected, onError);
  };

  const sendMessage: () => void = () => {
    const message: ChatMessage = {
      content: currentMessage,
      sender: user.username,
      messageType: 'MESSAGE'
    };

    stompClient.send('/app/send-message', {}, message);
    setCurrentMessage('');
  };
  const onMessageReceived: (payload: any) => void = payload => {
    const parsedPayload: ChatMessage = JSON.parse(payload.body);
    setChatMessages([...chatMessages, parsedPayload]);
  };

  const onConnected: () => void = () => {
    setUser({ ...user, connected: true, username: userName });
    stompClient.subscribe('/topic/public', onMessageReceived);
    const chatMessage: ChatMessage = { content: `User ${userName} hAas entered the chat`, messageType: 'JOIN', sender: userName };
    stompClient.send('/app/add-user', {}, chatMessage);
  };

  const onError = (error: any) => {
    console.log(error);
  };

  return (
    <div className="container">
      {user.connected ? (
        <div className={'w-screen h-screen bg-gray-800 flex justify-center items-center flex-col gap-2'}>
          <div className={'flex text-center p-4 tex-white text-3xl text-white'}>Welcome to the chat {user.username}</div>
          {chatMessages.map(message => {
            return (
              <div className={'flex gap-2'} key={message.sender}>
                <p className={'text-white text-base'}>{message.sender}</p>
                <p className={'text-white text-base'}>{message.content}</p>
              </div>
            );
          })}
          <input
            type="text"
            className="mt-1 p-2 border rounded-md focus:outline-none focus:ring focus:border-blue-300 bg-blue-100"
            onChange={event => setCurrentMessage(event.target.value)}
          />
          <button className={'hover:scale-105 transition-all bg-yellow-600 hover:bg-yellow-700 rounded-xl p-2'} onClick={sendMessage}>
            Send message
          </button>
        </div>
      ) : (
        <div className={'w-screen h-screen bg-gray-800 flex justify-center items-center'}>
          <div className={'flex gap-4 flex-col'}>
            <p className="text-white text-xl">Type in your username to enter the chat:</p>
            <input
              type="text"
              id="username"
              name="username"
              className="mt-1 p-2 w-full border rounded-md focus:outline-none focus:ring focus:border-blue-300 bg-blue-100"
              onChange={event => setUserName(event.target.value)}
            />
            <button className={'hover:scale-105 transition-all bg-yellow-600 hover:bg-yellow-700 rounded-xl'} onClick={registerUser}>
              Enter the chat
            </button>
          </div>
        </div>
      )}
    </div>
  );
}
