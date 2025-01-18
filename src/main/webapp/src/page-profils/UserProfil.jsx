import React from "react";
import UserUpdate from "./UserUpdate.jsx";
import UpdatePassword from "./UpdatePassword.jsx";


export default function UserProfil(){

    return (
            <>
            <h1>Page Profil User</h1>
                <div className="container-update">
                <UserUpdate/>
                <UpdatePassword/>
                </div>
            </>
    )
}