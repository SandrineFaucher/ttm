import React from "react";
import './privacyPolicy.css'

const PrivacyPolicy = () => {
    return (
        <div className= "page-privacy-policy" >
            <h1>Politique de confidentialité</h1>
            <p><strong>Dernière mise à jour :</strong> 25 août 2025</p>

            <h2>1. Données collectées</h2>
            <p>
                Nous collectons les informations suivantes : pseudo, adresse e-mail, mot de passe
                (chiffré), ville, département, région, disponibilités horaires, photo de profil,
                documents téléchargés (PDF, Word, etc.), et messages envoyés via la messagerie interne.
            </p>

            <h2>2. Finalités de la collecte</h2>
            <p>
                Ces données sont utilisées pour créer et gérer votre compte, permettre la mise en relation
                entre utilisateurs, gérer vos préférences et disponibilités, faciliter l’échange de
                messages et de documents, et améliorer nos services et la sécurité du site.
            </p>

            <h2>3. Partage des données</h2>
            <p>
                Vos données ne sont pas revendues à des tiers et peuvent être transmises uniquement à
                des prestataires techniques (hébergement, sécurité, maintenance, administrateur de l&apos;association) dans le cadre de nos services.
            </p>

            <h2>4. Conservation des données</h2>
            <p>
                Vos données sont conservées tant que votre compte est actif. En cas de suppression de
                votre compte, vos données seront supprimées ou anonymisées sous un délai de un mois.
            </p>

            <h2>5. Sécurité</h2>
            <p>
                Nous mettons en œuvre des mesures techniques pour protéger vos informations personnelles
                contre tout accès non autorisé, modification ou divulgation.
            </p>

            <h2>6. Vos droits</h2>
            <p>
                Vous disposez des droits suivants : accéder à vos données, les rectifier, supprimer
                votre compte et vos données, limiter ou vous opposer au traitement de vos données.
                Pour exercer vos droits, contactez-nous à : <strong>accompagnement@initiativedeuxsevres.fr</strong>
            </p>

            <h2>7. Cookies et suivi</h2>
            <p>
                Le site peut utiliser des cookies pour améliorer la navigation et mesurer l’audience.
                Vous pouvez gérer vos préférences dans les paramètres de votre navigateur.
            </p>

            <h2>8. Contact</h2>
            <p>
                Pour toute question concernant cette politique, contactez-nous à :
                <strong> accompagnement@initiativedeuxsevres.fr</strong>
            </p>
        </div>
    );
};

export default PrivacyPolicy;
