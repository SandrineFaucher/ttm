import React from 'react';
import PropTypes from 'prop-types';
import "./customSelect.css";

const CustomSelect = ({
                          label,
                          name,
                          options,
                          value,
                          onChange,
                          placeholder,
                          className,
                      }) => {
    return (
        <div className={`custom-select ${className}`}>
            {label && <label htmlFor={name}>{label}</label>}
            <select value={value} onChange={(e) => onChange(e.target.value)}>
                <option value="" disabled>
                    {placeholder}
                </option>
                {options.map((option) => (
                    <option key={option.value} value={option.value}>
                        {option.label}
                    </option>
                ))}
            </select>
        </div>
    );
};

CustomSelect.propTypes = {
    options: PropTypes.arrayOf(
        PropTypes.shape({
            label: PropTypes.string.isRequired,
            value: PropTypes.oneOfType([PropTypes.string, PropTypes.number]).isRequired,
        })
    ).isRequired,
    label : PropTypes.string,
    value: PropTypes.oneOfType([PropTypes.string, PropTypes.number]).isRequired,
    onChange: PropTypes.func.isRequired,
    name: PropTypes.string,
    placeholder: PropTypes.string,
    className: PropTypes.string,
};

export default CustomSelect;
