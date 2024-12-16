import React from "react";
import PropTypes from "prop-types";
import "./customImput.css";

const CustomInput = ({
                         label,
                         type,
                         name,
                         value,
                         onChange,
                         placeholder,
                         required = false,
                         className
                     }) => {
    return (
        <div className={`custom-input ${className}`}>
            {label && <label htmlFor={name}>{label}</label>}
            <input
                id={name}
                type={type}
                name={name}
                value={value}
                onChange={onChange}
                placeholder={placeholder}
                required={required}
                className="input-field"
            />
        </div>
    );
};

CustomInput.propTypes = {
    label : PropTypes.string,
    type : PropTypes.string,
    name : PropTypes.string,
    value :PropTypes.oneOfType([
    PropTypes.string,
    PropTypes.number
]).isRequired,
    onChange: PropTypes.func.isRequired,
    placeholder: PropTypes.string,
    required: PropTypes.bool,
    className: PropTypes.string
}
export default CustomInput;