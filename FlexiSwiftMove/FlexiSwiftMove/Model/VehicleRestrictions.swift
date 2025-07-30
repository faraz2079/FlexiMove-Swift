import Foundation

struct VehicleRestrictions: Codable {
    let minAge: Int
    let maxBookingTimeMinutes: Int
    let maxPassengers: Int?
    let maxDistanceKm: Double?
    let requiredLicense: String
}
