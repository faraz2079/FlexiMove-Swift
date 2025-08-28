import Foundation

class MockVehicleService: VehicleFetching {
    func fetchNearbyVehicles(address city: String, completion: @escaping (Result<[NearbyVehicle], Error>) -> Void) {
        let mockVehicles = [
            NearbyVehicle(
                id: 1,
                vehicleModel: "E-Scooter X",
                providerName: "Bolt",
                vehicleType: "Scooter",
                status: "Available",
                priceAmount: 3.99,
                billingModel: "Per Minute",
                address: "Kampstraße 45, Dortmund",
                latitude: 51.5142,
                longitude: 7.4660,
                distanceInKm: 1.2,
                averageVehicleRating: 4.5,
                averageProviderRating: 4.0,
                restrictions: VehicleRestrictions(
                    minAge: 21,
                    maxBookingTimeMinutes: 2,
                    maxPassengers: 4,
                    maxDistanceKm: 2,
                    requiredLicense: "Class B"
                )
            ),
            NearbyVehicle(
                id: 2,
                vehicleModel: "E-Car Mini",
                providerName: "ShareNow",
                vehicleType: "Car",
                status: "Available",
                priceAmount: 15.0,
                billingModel: "Per Hour",
                address: "Hansastraße 12, Dortmund",
                latitude: 51.5150,
                longitude: 7.4680,
                distanceInKm: 2.3,
                averageVehicleRating: 4.2,
                averageProviderRating: 4.3,
                restrictions: VehicleRestrictions(
                    minAge: 21,
                    maxBookingTimeMinutes: 2,
                    maxPassengers: 4,
                    maxDistanceKm: 2,
                    requiredLicense: "Class B"
                )
            )
        ]
        DispatchQueue.main.asyncAfter(deadline: .now() + 0.5) {
            completion(.success(mockVehicles))
        }
    }
}
