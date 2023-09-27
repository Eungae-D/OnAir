import React, { useState, useEffect } from "react";
import styles from "./ChatModal.module.css";
import { RootState } from "../../store";
import { useSelector, useDispatch } from "react-redux";
import { sendData, socketConnection } from "../../utils/socket.atom";
import { addChatMessage } from "../../store";

type ChatModalProps = {
  isOpen: boolean;
  onClose: () => void;
};

export const ChatModal: React.FC<ChatModalProps> = ({ isOpen, onClose }) => {
  const dispatch = useDispatch(); // dispatch 함수를 가져옵니다.

  const nickname = useSelector((state: RootState) => state.user.nickname);
  const messages = useSelector((state: RootState) => state.chat);

  const profileImage = useSelector(
    (state: RootState) => state.user.profileImage
  );

  const [message, setMessage] = useState<string>("");

  const clickSubmit = () => {
    if (!message) return;

    sendData("/app/chat", {
      content: message,
      sender: nickname,
      senderImage: profileImage,
    });

    setMessage("");
  };

  return (
    <div className={isOpen ? styles.modal : `${styles.modal} ${styles.closed}`}>
      <button className={styles.closeButton} onClick={onClose}>
        x
      </button>
      <h2 className={styles.chatHeader}>대화방</h2>
      <div className={styles.chatMessages}>
        {messages.map((msg, index) => (
          <div key={index} className={styles.chatMessage}>
            <img src={msg.senderImage} alt={msg.sender} />
            <span className={styles.username}>{msg.sender}</span>:
            <span className={styles.message_content}>{msg.content}</span>
          </div>
        ))}
      </div>
      <div className={styles.chatInputContainer}>
        <input
          type="text"
          placeholder="메시지를 입력하세요..."
          className={styles.chatInput}
          value={message}
          onChange={e => setMessage(e.target.value)}
          onKeyPress={e => {
            if (e.key === "Enter") {
              clickSubmit();
            }
          }}
        />
        <button className={styles.chatButton} onClick={clickSubmit}>
          전송
        </button>
      </div>
    </div>
  );
};

export default ChatModal;
