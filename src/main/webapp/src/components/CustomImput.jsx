import React from "react";
import PropTypes from "prop-types";
import "./customImput.css";
import {text} from "@fortawesome/fontawesome-svg-core";

const CustomInput = ({
                         label,
                         type = "text",
                         name,
                         value,
                         onChange,
                         placeholder,
                         required = false,
                         className,
                         ariaDescribedBy,
                     }) => {

    const inputId = `${name}-input`;
    const labelId = `${name}-label`;

    return (
        <div className={`custom-input ${className}`}>
            {label && (
                <label id={labelId} htmlFor={inputId}>
                    {label}
                </label>
            )}
            <input
                id={name}
                type={type}
                name={name}
                value={value}
                onChange={onChange}
                placeholder={placeholder}
                required={required}
                className="input-field"
                aria-labelledby={label ? labelId : undefined}
                aria-describedby={ariaDescribedBy}
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
    className: PropTypes.string,
    ariaDescribedBy: PropTypes.string
}
export default CustomInput;