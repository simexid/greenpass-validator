/**
 * Greenpass model
 */
export class GreenPassModel {
    name: string;
    surname: string;
    birthDate: Date;
    vendorName: string;
    productName: string;
    diseaseAgent: string;
    prophylaxis: string;
    dose: number;
    overallDose: number;
    date: Date;
    issuer: string;
    identifier: string;
    type: string;
    isValid: boolean;
}