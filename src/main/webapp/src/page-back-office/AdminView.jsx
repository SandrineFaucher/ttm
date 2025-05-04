import React from "react";
import FormRegister from "./FormRegister.jsx";
import './adminView.css';

export default function AdminView(){
    return (
        <section className="admin-container">
            <h1>Back-office</h1>
            <section className="block">
            <FormRegister/>
            </section>
        </section>
    )
}

