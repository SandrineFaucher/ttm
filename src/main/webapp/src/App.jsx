import React from "react";
import { Routes, Route } from 'react-router-dom';
import Layout from "./layout/Layout.jsx";
import Home from "./page-home/Home.jsx";
import Profils from "./page-profils/Profils.jsx";
import Messagerie from "./page-messagerie/Messagerie.jsx";
import Document from "./page-boite-a-outils/Document.jsx";
import AdminView from "./page-back-office/AdminView.jsx";


export default function App() {


  return (
      <Routes>
        <Route path={"/"} element={<Layout />}>
          <Route index element={<Home/>}/>
          <Route path="profils" element={<Profils/>}/>
          <Route path="messagerie" element={<Messagerie/>}/>
          <Route path="boite-a-outils" element={<Document/>}/>
          <Route path="adminView" element={<AdminView/>}/>
        </Route>
      </Routes>
    );
}