import PropTypes from "prop-types";
import "./customTextarea.css";

const CustomTextarea = ({
                            label,
                            name,
                            value,
                            onChange,
                            placeholder,
                            required = false,
                            className,
                            rows = 4
                        }) => {
    return (
        <div className={`custom-textarea ${className}`}>
            {label && <label htmlFor={name}>{label}</label>}
            <textarea
                id={name}
                name={name}
                value={value}
                onChange={onChange}
                placeholder={placeholder}
                required={required}
                rows={rows}
                className="textarea-field"
            />
        </div>
    );
};

CustomTextarea.propTypes = {
    label: PropTypes.string,
    name: PropTypes.string.isRequired,
    value: PropTypes.string.isRequired,
    onChange: PropTypes.func.isRequired,
    placeholder: PropTypes.string,
    required: PropTypes.bool,
    className: PropTypes.string,
    rows: PropTypes.number
};

export default CustomTextarea;