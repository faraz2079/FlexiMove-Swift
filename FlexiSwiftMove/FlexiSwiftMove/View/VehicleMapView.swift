import SwiftUI
import MapKit

struct VehicleMapView: View {
    let vehicles: [NearbyVehicle]

//    @State private var region = MKCoordinateRegion(
//        center: CLLocationCoordinate2D(latitude: 51.515, longitude: 7.465), // Example center
//        span: MKCoordinateSpan(latitudeDelta: 0.1, longitudeDelta: 0.1)
//    )
    
    @State private var region: MKCoordinateRegion

    init(vehicles: [NearbyVehicle]) {
        self.vehicles = vehicles
        if let first = vehicles.first {
            _region = State(initialValue: MKCoordinateRegion(
                center: CLLocationCoordinate2D(latitude: first.latitude, longitude: first.longitude),
                span: MKCoordinateSpan(latitudeDelta: 0.05, longitudeDelta: 0.05)
            ))
        } else {
            _region = State(initialValue: MKCoordinateRegion(
                center: CLLocationCoordinate2D(latitude: 51.515, longitude: 7.465),
                span: MKCoordinateSpan(latitudeDelta: 0.1, longitudeDelta: 0.1)
            ))
        }
    }

    var annotations: [NearbyVehicleAnnotation] {
        vehicles.map { NearbyVehicleAnnotation(vehicle: $0) }
    }

    var body: some View {
        Map(coordinateRegion: $region, annotationItems: annotations) { annotation in
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
        .edgesIgnoringSafeArea(.all)
        .navigationTitle("Map of Vehicles")
    }
}

