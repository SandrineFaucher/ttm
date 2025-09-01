import "./customCard.css"
import PropTypes from "prop-types";
import {AuthContext} from "../context/AuthContext.jsx";
import {useContext} from "react";

const CustomCard =({ clickable, onClick, title, image, availability, sectors, accompaniements, city, department, region, content, userRole}) =>{
console.log("Image :" ,image)
    const {auth } = useContext(AuthContext);

 return (
        <div className={`body-card ${clickable ? "cursor-pointer" : ""}`}

             onClick={clickable ? onClick : undefined}>
            <h2 className="pseudo-user">{title}</h2>
            <img className="profil-image" src={image} alt="photo-profil"/>
            <p className="location">{region} <br/> {city}{department}</p>
            <div className="card-container">
                        <h5>Disponibilités :</h5>
                        <p>{availability}</p>
                        <h5>Description :</h5>
                        <p>{content}</p>
                        <div >
                            <h5>Secteurs / réseaux :</h5>
                            <p>{sectors}</p>
                            <h5>Accompagnement :</h5>
                            <p>{accompaniements}</p>
                        </div>


            </div>
        </div>
 )
};
CustomCard.propTypes = {
    clickable: PropTypes.bool,
    onClick: PropTypes.func,
    title: PropTypes.string,
    availability: PropTypes.string,
    sectors: PropTypes.string,
    accompaniements: PropTypes.string,
    city: PropTypes.string,
    department: PropTypes.string,
    region: PropTypes.string,
    content: PropTypes.string,
    image:PropTypes.string
}
export default CustomCard;