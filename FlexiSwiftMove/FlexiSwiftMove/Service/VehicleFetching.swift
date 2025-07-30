import Foundation

protocol VehicleFetching {
    func fetchNearbyVehicles(address: String, completion: @escaping (Result<[NearbyVehicle], Error>) -> Void)
}
