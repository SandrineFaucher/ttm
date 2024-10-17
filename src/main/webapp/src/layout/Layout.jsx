import React, { useState } from "react";
import {Outlet, Link} from "react-router-dom";
import "./layout.css";
import logo from "../assets/images/logo.png";
import ttm_rose from "../assets/images/ttm_rose.png";
import hologramme_ttm from "../assets/images/hologramme_ttm.png";

import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'; 
import { faHouse, faMessage, faAddressCard, faToolbox, faUser } from '@fortawesome/free-solid-svg-icons';
import { faInstagram, faTiktok, faSquareFacebook, faLinkedin } from '@fortawesome/free-brands-svg-icons';

export default function Layout() {
    const [isNavOpen, setIsNavOpen] = useState(false);

    const toggleNav = () => {
        setIsNavOpen(!isNavOpen)
    };

    return (
        <>
        <header>
        <Link to="/"><img src={logo} alt="logo-initiative-deux-sèvres" className="logo-entreprise"/></Link>
        <Link to="/"><div className="div-logo-ttm"><img src={ttm_rose} alt="logo-ttm" className="logo-ttm"/></div></Link>
            <nav className ={`navbar ${isNavOpen ? 'active' : ''}`}>
            <div className="burger-icon" onClick={toggleNav}>
             {isNavOpen ? '\u2716' : '\u2630' }
            </div>
            
                <ul className={`nav-links ${isNavOpen ? 'open' : ''}`}>
                    
                    <li>
                        <Link to="/"><FontAwesomeIcon icon={faHouse} className="icon"/><div className="page-name">Accueil</div></Link>
                    </li>
                    <li>
                        <Link to="/Profil"><FontAwesomeIcon icon={faAddressCard} className="icon"/><div className="page-name">Profil</div></Link>
                    </li>
                    <li>
                        <Link to="/Messagerie"><FontAwesomeIcon icon={faMessage} className="icon"/><div className="page-name">Messagerie</div></Link>
                    </li>
                    <li>
                        <Link to="/boite-a-outils"><FontAwesomeIcon icon={faToolbox} className="icon"/><div className="page-name">Boîte à Outils</div></Link>
                    </li>
                </ul>
            <div className="icon-user">
                <FontAwesomeIcon icon={faUser} />
            </div>
            </nav>
        </header>
            <main>
                <Outlet/>
            </main>
            <footer className="footer-line">
                <div className="footer-bloc1">
                    <img src={hologramme_ttm} alt="logo-mini-ttm" />
                </div>
                <div className="footer-bloc2">
                    <p className="text-bloc2">Votre plateforme : <br/>Initiative Deux-Sèvres</p>
                </div>
                <div className="footer-bloc3">
                    <p><a href="tel:+0679875609">0679875609</a><br/><a href="mailto:accompagnement@initiativedeuxsevres.fr" >accompagnement@initiativedeuxsevres.fr</a></p>
                </div>
                <div className="footer-bloc4">
                    <Link to="" className="social-link" target="_blank"><FontAwesomeIcon icon={faInstagram} className="social-icon"/></Link>
                    <Link to="" className="social-link" target="_blank"><FontAwesomeIcon icon={faTiktok} className="social-icon" /></Link>
                    <Link to="https://www.facebook.com/profile.php?id=61556616180678" className="social-link" target="_blank"><FontAwesomeIcon icon={faSquareFacebook} className="social-icon"/></Link>
                    <Link to="https://www.linkedin.com/company/initiative-deux-sevres/posts/?feedView=all" className="social-link" target="_blank"><FontAwesomeIcon icon={faLinkedin} className="social-icon"/></Link>
                </div>
            </footer>
        </>
    );
}