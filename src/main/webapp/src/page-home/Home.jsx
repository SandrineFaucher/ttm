import React, {useState} from "react";
import "./home.css";
import {Link, useNavigate} from "react-router-dom";




export default function Home(){
    const navigate = useNavigate();
    const [accepted, setAccepted] = useState(false)

    const goToLogin = () => {
        navigate("/login");
    };

    const handleCheckboxChange = (e) => {
        setAccepted(e.target.checked);
    };

    return (
      <>
            <h1>Bienvenue</h1>
          <aside>
              <p className="text">Trouve ton match est une appli qui vous permettra de trouver <strong>LA </strong> personne pour vous accompagner et
                  vous soutenir, une épaule sur laquelle vous reposer.</p>
              <p className="text">Le but étant de créer un climat de confiance avec cette personne afin que vous puissiez vous tourner vers elle
                  lorsque vous avez des soucis ou des besoins.</p>
              <p className="text">Ce parrain / marraine peut être un chef d&apos;entreprise, ou d&apos;un cadre dirigeant ayant de l&apos;expérience
                  et un réseau important pour faire appel à des partenaires experts dans vos besoins.</p>

              <section className="connection">
                <div className="privacyPolicy">
                  <input
                      type="checkbox"
                      id="acceptPrivacy"
                      checked={accepted}
                      onChange={handleCheckboxChange}
                      style={{ accentColor: "#e31766" }}
                  />
                  <label htmlFor="acceptPrivacy">
                      J&apos;accèpte la <Link to="/privacy">politique de confidentialité</Link>
                  </label>
                </div>
                  <button className="btn-accueil" onClick={goToLogin} disabled={!accepted}>Je me connecte</button>
              </section>
          </aside>
      </>
    )
}