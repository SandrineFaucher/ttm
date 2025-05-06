import React, {useState} from "react";
import { Routes, Route } from 'react-router-dom';
import Layout from "./layout/Layout.jsx";
import Home from "./page-home/Home.jsx";
import Profils from "./page-profils/Profils.jsx";
import Messagerie from "./page-messagerie/Messagerie.jsx";
import Document from "./page-boite-a-outils/Document.jsx";
import AdminView from "./page-back-office/AdminView.jsx";
import FormLogin from "./page-home/FormLogin.jsx";
import { AuthProvider } from "./context/AuthContext.jsx";
import UserProfil from "./page-profils/UserProfil.jsx";
import DetailCard from "./page-profils/DetailCard.jsx";
import {UserProvider} from "./context/UserContext.jsx";
import {NotificationProvider} from "./context/NotificationContext.jsx";
import ListOfConversations from "./page-messagerie/ListOfConversations.jsx";



export default function App() {


  return (
<NotificationProvider>
  <AuthProvider>
    <UserProvider>
      <Routes>
        <Route path={"/"} element={<Layout />}>
          <Route index element={<Home/>}/>
          <Route path="profils" element={<Profils/>}/>
          <Route path="messagerie/:id" element={<Messagerie/>}/>
          <Route path="boite-a-outils" element={<Document/>}/>
          <Route path="adminView" element={<AdminView/>}/>
          <Route path="/login" element={<FormLogin />} />
          <Route path="/userProfil" element={<UserProfil/>} />
          <Route path="/detailCard/:id" element={<DetailCard/>} />
          <Route path="/listeOfConversations" element={<ListOfConversations/>} />
        </Route>
      </Routes>
    </UserProvider>
  </AuthProvider>
</NotificationProvider>
    );
}