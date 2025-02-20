import "./customCard.css"
import PropTypes from "prop-types";

const CustomCard =({ title, sectors, accompaniements, city, department, region, description}) =>{
    return (
        <div className="body-card">
            <p className="location">{region} {city}{department}</p>
            <h2 className="pseudo-user">{title}</h2>
            <img alt="photo-profil"/>
            <p>{sectors}</p>
            <p>{accompaniements}</p>
            <p>{description}</p>
        </div>
    )
};
CustomCard.prototype = {
    title : PropTypes.string,
    sectors: PropTypes.string,
    accompaniements: PropTypes.string,
    city: PropTypes.string,
    department: PropTypes.string,
    region: PropTypes.string,
    description: PropTypes.string
}
export default CustomCard;