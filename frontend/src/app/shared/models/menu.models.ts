/**
 * Interfaces TypeScript que reflejan exactamente los DTOs del backend Java.
 * Si cambias un DTO en el backend, actualiza también su interfaz aquí.
 *
 * Usamos 'interface' (no 'class') porque son solo tipos — no necesitan lógica.
 */

// ----- Catálogo -----

export type CategoryType =
  | 'TAPAS_CLASICAS'
  | 'TAPAS_PREMIUM'
  | 'COCINA'
  | 'HORNO'
  | 'COMPARTIR'
  | 'POSTRES'
  | 'ENCARGOS';

export interface Product {
  id: number;
  name: string;
  description: string | null;
  basePrice: number;
  minimumUnits: number;
  available: boolean;
  exclusive: boolean;
  imageUrl: string | null;
  categoryType: CategoryType;
  categoryDisplayName: string;
}

export interface PackComponent {
  productId: number | null;    // null = "a elección del chef"
  productName: string | null;
  quantity: number;
  description: string;
}

export interface Pack {
  id: number;
  name: string;
  description: string;
  priceMin: number;
  priceMax: number | null;     // null = precio fijo (sin rango)
  available: boolean;
  components: PackComponent[];
}

export type WeeklySpecialType = 'TAPA' | 'MINI';

export interface WeeklySpecial {
  id: number;
  type: WeeklySpecialType;
  weekOf: string;              // 'YYYY-MM-DD'
  product: Product;
}

/** Lo que devuelve GET /api/menu — todo en una sola llamada */
export interface MenuResponse {
  productsByCategory: Record<string, Product[]>;  // clave = categoryDisplayName
  packs: Pack[];
  weeklySpecials: WeeklySpecial[];
}

// ----- Pedidos -----

export type DeliveryType  = 'DELIVERY' | 'PICKUP';
export type OrderItemType = 'PRODUCT'  | 'PACK';
export type OrderStatus   = 'PENDING' | 'CONFIRMED' | 'IN_PREPARATION' | 'READY' | 'DELIVERED' | 'CANCELLED';

export interface OrderItem {
  id: number;
  itemType: OrderItemType;
  productId: number | null;
  packId: number | null;
  itemName: string;
  unitPrice: number;
  quantity: number;
  subtotal: number;
}

export interface Order {
  id: number;
  orderNumber: string;
  customerName: string;
  customerEmail: string;
  customerPhone: string;
  deliveryType: DeliveryType;
  deliveryAddress: string | null;
  deliveryDate: string;        // ISO datetime string
  status: OrderStatus;
  notes: string | null;
  subtotal: number;
  deliveryFee: number;
  total: number;
  createdAt: string;
  items: OrderItem[];
}

// ----- Requests (lo que enviamos al backend) -----

export interface OrderItemRequest {
  type: OrderItemType;
  itemId: number;
  quantity: number;
}

export interface PlaceOrderRequest {
  customerName: string;
  customerEmail: string;
  customerPhone: string;
  deliveryType: DeliveryType;
  deliveryAddress?: string;
  deliveryDate: string;        // ISO datetime: '2026-05-01T18:00:00'
  notes?: string;
  items: OrderItemRequest[];
}

// ----- Auth -----

export interface LoginRequest {
  username: string;
  password: string;
}

export interface AuthResponse {
  token: string;
  username: string;
}
