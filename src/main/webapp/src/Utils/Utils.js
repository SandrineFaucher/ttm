import {ObjectId} from 'mongodb';

// Convertir un timestamp en ObjectId
export const convertTimestampToObjectId = (timestamp) => {
    return new ObjectId(Math.floor(timestamp) * 1000); // Convertit en millisecondes
};