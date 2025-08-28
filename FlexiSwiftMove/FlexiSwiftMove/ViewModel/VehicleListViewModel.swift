import Foundation

class VehicleListViewModel: ObservableObject {
    @Published var vehicles: [NearbyVehicle] = []
    @Published var isLoading = true
    @Published var error: String?
        
    private let vehicleFetcher: VehicleFetching
    
    init(vehicleFetcher: VehicleFetching = MockVehicleService()) { // Change to VehicleService later
        self.vehicleFetcher = vehicleFetcher
    }

    func fetchNearbyVehicles(for address: String) {
        vehicleFetcher.fetchNearbyVehicles(address: address) { [weak self] result in
            DispatchQueue.main.async {
                switch result {
                case .success(let vehicles):
                    self?.vehicles = vehicles
                    self?.error = nil
                case .failure(let error):
                    self?.vehicles = []
                    self?.error = "failed to load vehicles: \(error.localizedDescription)"
                }
                self?.isLoading = false // important, otherwise it keeps on being loading
            }
        }
    }
}
