import React from 'react';
import PropTypes from 'prop-types';
import "./customSelect.css";

const CustomSelect = ({
                          options,
                          value,
                          onChange,
                          name,
                          placeholder,
                          className,
                      }) => {
    return (
        <div className={`custom-select ${className || ''}`}>
            <select
                name={name}
                value={value}
                onChange={(e) => onChange(e.target.value)}
                className="select-input"
            >
                {placeholder && (
                    <option value="" disabled>
                        {placeholder}
                    </option>
                )}
                {options.map((option, index) => (
                    <option key={index} value={option.value}>
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
    value: PropTypes.oneOfType([PropTypes.string, PropTypes.number]).isRequired,
    onChange: PropTypes.func.isRequired,
    name: PropTypes.string,
    placeholder: PropTypes.string,
    className: PropTypes.string,
};

export default CustomSelect;
