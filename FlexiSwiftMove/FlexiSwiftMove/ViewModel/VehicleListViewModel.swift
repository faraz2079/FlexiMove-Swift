import Foundation

class VehicleListViewModel: ObservableObject {
    @Published var vehicles: [NearbyVehicle] = []
    @Published var isLoading = true
    @Published var error: String?

    private let service = VehicleService()

    func loadNearbyVehicles(for address: String) {
        isLoading = true
        service.fetchNearbyVehicles(address: address) { result in
            DispatchQueue.main.async {
                self.isLoading = false
                switch result {
                case .success(let vehicles):
                    self.vehicles = vehicles
                case .failure(let error):
                    self.error = error.localizedDescription
                }
            }
        }
    }
}
