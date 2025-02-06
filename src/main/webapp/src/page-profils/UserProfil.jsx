import React from "react";
import UserUpdate from "./UserUpdate.jsx";
import UpdatePassword from "./UpdatePassword.jsx";
import FormProfil from "./FormProfil.jsx";
import "./userProfil.css";


export default function UserProfil(){

    return (
            <>
            <h1>Mon Profil </h1>
                <div className="container">
                    <div className="block">
                        <h2>Remplir mon profil</h2>
                        <FormProfil/>
                    </div>
                    <div className="block">
                        <h2>Modifier mes données</h2>
                        <div className="component">
                            <UserUpdate/>
                        </div>
                        <div className="component">
                            <UpdatePassword/>
                        </div>
                    </div>

                </div>
            </>
    )
}