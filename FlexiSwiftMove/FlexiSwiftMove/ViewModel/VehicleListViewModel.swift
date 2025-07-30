import Foundation

class VehicleListViewModel: ObservableObject {
    @Published var vehicles: [NearbyVehicle] = []
    @Published var isLoading = true
    @Published var error: String?
    
    private let VehicleFetcher: VehicleFetching
    
    init(VehicleFetcher: VehicleFetching) {
        self.VehicleFetcher = VehicleFetcher
    }

    private let service = VehicleService()

    func fetchNearbyVehicles(for address: String) {
        VehicleFetcher.fetchNearbyVehicles(address: address) { [weak self] result in
            DispatchQueue.main.async {
                switch result {
                case .success(let vehicles):
                    self?.vehicles = vehicles
                    self?.error = nil
                case .failure(let error):
                    self?.vehicles = []
                    self?.error = "failed to load vehicles: \(error.localizedDescription)"
                }
            }
        }
    }
}
