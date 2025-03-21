import "./customCard.css"
import PropTypes from "prop-types";

const CustomCard =({ clickable, onClick, title, image, availability, sectors, accompaniements, city, department, region, description}) =>{
console.log("Image :" ,image)
    return (
        <div className={`body-card ${clickable ? "cursor-pointer" : ""}`}
        onClick={clickable ? onClick : undefined} >
            <p className="location">{region} {city}{department}</p>
            <h2 className="pseudo-user">{title}</h2>
            <img className="profil_image" src={image} alt="photo-profil"/>
            <div className="small-containers">
            <h5>Disponibilités :</h5>
            <p>{availability}</p>
            </div>
            <div className="small-containers">
            <h5>Secteurs / réseaux :</h5>
            <p>{sectors}</p>
            </div>
            <div className="small-containers">
            <h5>Accompagnements :</h5>
            <p>{accompaniements}</p>
            </div>
            <div className="small-containers">
            <h5>Le petit mot en plus :</h5>
            <p>{description}</p>
            </div>
        </div>
    )
};
CustomCard.prototype = {
    clickable: PropTypes.bool,
    onClick: PropTypes.func,
    title : PropTypes.string,
    availability: PropTypes.string,
    sectors: PropTypes.string,
    accompaniements: PropTypes.string,
    city: PropTypes.string,
    department: PropTypes.string,
    region: PropTypes.string,
    description: PropTypes.string,
    image:PropTypes.string
}
export default CustomCard;