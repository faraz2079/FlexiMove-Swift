import SwiftUI
import MapKit

struct VehicleMapView: View {
    
    let vehicle: [NearbyVehicle]
    
    @State private var region: MKCoordinateRegion
    
    init(vehicle: [NearbyVehicle]) {
        self.vehicle = vehicle
        if let first = vehicle.first {
            _region = State(initialValue: MKCoordinateRegion(center: CLLocationCoordinate2D(latitude: first.latitude, longitude: first.longitude), span: MKCoordinateSpan(latitudeDelta: 0.05, longitudeDelta: 0.05)
            ))
        } else {
            _region = State(initialValue:
                                MKCoordinateRegion(center: CLLocationCoordinate2D(latitude: 51.515, longitude: 7.465),
                                                   span: MKCoordinateSpan(latitudeDelta: 0.1, longitudeDelta: 0.1)))
        }
    }
    
    var annotation: [NearbyVehicleAnnotation] {
        vehicle.map {NearbyVehicleAnnotation(vehicle: $0)}
    }
    
    
    
    var body: some View {
        Map(coordinateRegion: $region, annotationItems: annotation) { annotation in
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
