import React from "react";
import UserList from "./UserList.jsx";
import "./profils.css";

export default function Profils(){

    return (
            <>
            <h1>Profils </h1>
                <div className="profils-container">
                    <UserList />
                </div>
            </>
    )
}