import SwiftUI
import MapKit

struct VehicleDetailView: View {
    
    let vehicle: NearbyVehicle
    
    var body: some View {
        
        Map(coordinateRegion: .constant(
            MKCoordinateRegion(
                center: CLLocationCoordinate2D(latitude: vehicle.latitude, longitude: vehicle.longitude),
                span: MKCoordinateSpan(latitudeDelta: 0.01, longitudeDelta: 0.01)
            )
        ),
            annotationItems: [NearbyVehicleAnnotation(vehicle: vehicle)]
        ) { annotation in
            MapAnnotation(coordinate: annotation.coordinate) {
                VStack(spacing: 4) {
                    Image(systemName: "car.fill")
                        .foregroundColor(.blue)
                        .font(.title2)
                    Text(annotation.title)
                        .font(.caption)
                        .padding(2)
                        .background(Color.white.opacity(0.8))
                        .cornerRadius(4)
                }
            }
        }
        VStack(alignment: .leading, spacing: 6) {
            Text(vehicle.vehicleModel).font(.headline)
            Text("Provider: \(vehicle.providerName)")
            Text("Type: \(vehicle.vehicleType)")
            Text("Status: \(vehicle.status)")
            Text("Address: \(vehicle.address)")
            Text("Provider Rating: \(vehicle.averageProviderRating)")
            Text("Max Number of Passengers: \(vehicle.restrictions.maxPassengers ?? 4)")
            Text("Required License: \(vehicle.restrictions.requiredLicense)")
            Text("Minimum Age: \(vehicle.restrictions.minAge)")
            Text("Price: €\(vehicle.priceAmount, specifier: "%.2f")")
            Text("Distance: \(vehicle.distanceInKm, specifier: "%.1f") km")
            Text("Rating: \(vehicle.averageVehicleRating, specifier: "%.1f") ★")
        }
        .padding(.vertical, 4)
        .navigationTitle(vehicle.vehicleModel)
    }
}


