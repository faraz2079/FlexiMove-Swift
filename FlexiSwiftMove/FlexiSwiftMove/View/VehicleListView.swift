import SwiftUI
import MapKit

struct VehicleListView: View {
    @StateObject private var viewModel = VehicleListViewModel()

    var body: some View {
        NavigationView {
            VStack {
                if viewModel.isLoading {
                    ProgressView("Loading vehicles...")
                } else if let error = viewModel.error {
                    Text("Error: \(error)")
                } else {
                    NavigationLink(destination: VehicleMapView(vehicles: viewModel.vehicles)) {
                        Text("🗺️ Show on Map")
                            .font(.headline)
                            .padding()
                    }
                    List(viewModel.vehicles) { vehicle in
                        VStack(alignment: .leading, spacing: 6) {
                            Text(vehicle.vehicleModel).font(.headline)
                            Text("Provider: \(vehicle.providerName)")
                            Text("Type: \(vehicle.vehicleType)")
                            Text("Price: €\(vehicle.priceAmount, specifier: "%.2f") / \(vehicle.billingModel)")
                            Text("Distance: \(vehicle.distanceInKm, specifier: "%.1f") km")
                            Text("Rating: \(vehicle.averageVehicleRating, specifier: "%.1f") ★")
                        }
                        .padding(.vertical, 4)
                    }
                }
            }
            .navigationTitle("Nearby Vehicles")
            .onAppear {
                viewModel.loadNearbyVehicles(for: "Dortmund")
            }
        }
    }
}
