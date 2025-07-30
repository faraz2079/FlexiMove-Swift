import Foundation

struct NearbyVehicle: Identifiable, Codable {
    let id: Int
    let vehicleModel: String
    let providerName: String
    let vehicleType: String
    let status: String
    let priceAmount: Double
    let billingModel: String
    let address: String
    let latitude: Double
    let longitude: Double
    let distanceInKm: Double
    let averageVehicleRating: Double
    let averageProviderRating: Double
    let restrictions: VehicleRestrictions

    enum CodingKeys: String, CodingKey {
        case id = "vehicleId"
        case vehicleModel
        case providerName
        case vehicleType
        case status
        case priceAmount
        case billingModel
        case address
        case latitude
        case longitude
        case distanceInKm
        case averageVehicleRating
        case averageProviderRating
        case restrictions
    }
}
