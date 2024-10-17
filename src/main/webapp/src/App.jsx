import React from "react";
import { Routes, Route } from 'react-router-dom';
import Layout from "./layout/Layout.jsx";
import Home from "./home/Home.jsx";
import Profil from "./profils/Profil.jsx";
import Messagerie from "./messagerie/Messagerie.jsx";
import Document from "./boite-a-outils/Document.jsx";


export default function App() {


  return (
      <Routes>
        <Route path={"/"} element={<Layout />}>
          <Route index element={<Home/>}/>
          <Route path="profil" element={<Profil/>}/>
          <Route path="messagerie" element={<Messagerie/>}/>
          <Route path="boite-a-outils" element={<Document/>}/>
        </Route>
      </Routes>
    );
}