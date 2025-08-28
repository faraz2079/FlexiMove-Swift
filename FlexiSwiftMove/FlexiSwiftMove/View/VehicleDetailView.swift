//
//  VehicleDetailView.swift
//  FlexiSwiftMove
//
//  Created by Faraz on 8/28/25.
//

import SwiftUI

struct VehicleDetailView: View {
//    @StateObject var viewModel: VehicleListViewModel
    
    let vehicle: NearbyVehicle
    
//    init() {
//        _viewModel = StateObject(wrappedValue: VehicleListViewModel(vehicleFetcher: MockVehicleService()))
//    }

    var body: some View {
        ScrollView {
            VStack(alignment: .leading, spacing: 6) {
                Text(vehicle.vehicleModel).font(.headline)
                Text("Provider: \(vehicle.providerName)")
                Text("Type: \(vehicle.vehicleType)")
                Text("Status: \(vehicle.status)")
                Text("Address: \(vehicle.address)")
                Text("Rating: \(vehicle.averageVehicleRating)")
                Text("Provider Rating: \(vehicle.averageProviderRating)")
                Text("Max Number of Passengers: \(vehicle.restrictions.maxPassengers ?? 4)")
                Text("Required License: \(vehicle.restrictions.requiredLicense)")
                Text("Minimum Age: \(vehicle.restrictions.minAge)")
                Text("Price: €\(vehicle.priceAmount, specifier: "%.2f")")
                Text("Distance: \(vehicle.distanceInKm, specifier: "%.1f") km")
                Text("Rating: \(vehicle.averageVehicleRating, specifier: "%.1f") ★")
            }
            .padding(.vertical, 4)
        }
        .navigationTitle("Vehicle Title")
    }
}
